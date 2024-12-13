import argparse
import json
import threading
import time
from datetime import datetime, timedelta

import pandas as pd
import pika
import requests
import keyboard


def read_csv(file_path):
    df = pd.read_csv(file_path, header=None)
    return df[0].tolist()


def delete_device_data(device_id):
    url = f"http://localhost/api/energy-management-monitor/device-data/deleteAll/{device_id}"
    response = requests.delete(url)
    if response.status_code == 200:
        print(f"Deleted previous data for device {device_id}")
    else:
        print(f"Failed to delete data for device {device_id}: {response.text}")


def setup_rabbitmq(device_id):
    connection = pika.BlockingConnection(pika.ConnectionParameters(
        host='localhost', port=5672, credentials=pika.PlainCredentials('guest', 'guest')
    ))
    channel = connection.channel()
    channel.queue_declare(queue='monitor-sensor-queue', durable=True)

    delete_device_data(device_id)

    return connection, channel


def send_to_rabbitmq(channel, message):
    channel.basic_publish(
        exchange='',
        routing_key='monitor-sensor-queue',
        body=message,
        properties=pika.BasicProperties(
            delivery_mode=2,
            content_type='application/json'
        )
    )


def data_sender_thread(values, channel, device_id, stop_event):
    timestamp = datetime.now()

    for i in range(1, len(values)):
        if stop_event.is_set():
            print("Stopping data sender thread.")
            break

        result = values[i] - values[i - 1]
        result = round(result, 2)
        timestamp += timedelta(hours=1)

        message = {
            "value": result,
            "timestamp": timestamp.isoformat(timespec='milliseconds'),
            "deviceId": device_id
        }

        message_json = json.dumps(message)

        send_to_rabbitmq(channel, message_json)
        print(message_json)

        time.sleep(1)


def main(file_path, device_id):
    values = read_csv(file_path)
    connection, channel = setup_rabbitmq(device_id)

    stop_event = threading.Event()

    try:
        thread = threading.Thread(target=data_sender_thread, args=(values, channel, device_id, stop_event))
        thread.start()

        print("Press 'q' to stop the data sender thread.")
        while thread.is_alive():
            if keyboard.is_pressed('q'):
                stop_event.set()
                break

        thread.join()
    finally:
        connection.close()


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Data Sender Script")
    parser.add_argument("--device_id", type=str, required=True, help="Device ID to include in the messages")
    parser.add_argument("--file_path", type=str, default="sensor.csv", help="Path to the CSV file with data")

    args = parser.parse_args()

    main(args.file_path, args.device_id)