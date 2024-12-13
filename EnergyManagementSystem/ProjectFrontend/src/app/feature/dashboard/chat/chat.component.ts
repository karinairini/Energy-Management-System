import {
  AfterViewChecked,
  Component,
  ElementRef,
  EventEmitter,
  OnDestroy,
  OnInit,
  Output,
  ViewChild
} from '@angular/core';
import {animate, style, transition, trigger} from '@angular/animations';
import {UserModel} from "../../../shared/models/user.model";
import {getUser} from "../../../core/service/user/user.service";
import {MessageModel} from "../../../shared/models/message.model";
import {map} from "rxjs";
import {ClientService} from "../../../core/service/client/client.service";
import {ClientModel} from "../../../shared/models/client.model";
import {CookieService} from "ngx-cookie-service";

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss'],
  animations: [
    trigger('chatBubbleAnimation', [
      transition(':enter', [
        style({transform: 'translateY(100%)', opacity: 0}),
        animate('300ms ease-out', style({transform: 'translateY(0)', opacity: 1}))
      ])
    ])
  ]
})
export class ChatComponent implements OnInit, OnDestroy, AfterViewChecked {

  private socket: WebSocket | undefined;
  loggedUser: UserModel = getUser();
  firstAdmin!: ClientModel | null;
  selectedUser: ClientModel | null = null;
  users: ClientModel[] = [];
  message: string = '';
  messages: { text: string, sent: boolean, read?: boolean }[] = [];
  @Output() messageSent = new EventEmitter<string>();
  isStatusTyping: boolean = false;
  private lastTypingTime: number = 0;
  private typingTimeout: any = null;
  @ViewChild('chatDisplay') chatDisplay!: ElementRef;
  private userAtBottom: boolean = true;

  constructor(
    private clientService: ClientService,
    private cookieService: CookieService
  ) {
  }

  ngOnInit(): void {
    this.getFirstAdmin();
    this.fetchUsers();
  }

  ngOnDestroy(): void {
    this.socket?.close();
    if (this.typingTimeout) {
      clearTimeout(this.typingTimeout);
    }
  }

  ngAfterViewChecked(): void {
    if (this.userAtBottom) {
      this.scrollToBottom();
    }
  }

  connectWebSocket(): void {
    this.socket = new WebSocket(`ws://localhost/api/energy-management-chat/ws/chat?fromUserId=${this.loggedUser.id}&toUserId=${this.selectedUser!.id}`);

    this.socket.onopen = () => {
      const jwtToken = this.cookieService.get('jwt-token');
      this.socket?.send(JSON.stringify({status: "auth", token: jwtToken}));
      console.log("WebSocket connection for chat established.");
    };

    this.socket.onmessage = (event) => {
      const message = JSON.parse(event.data);
      this.handleIncomingMessage(message);
    };

    this.socket.onclose = () => {
      console.log("WebSocket connection for chat closed.");
    };
  }

  getFirstAdmin(): void {
    this.clientService.getAdmins()
      .pipe(map(admins => admins.length > 0 ? admins[0] : null))
      .subscribe(firstAdmin => {
        this.firstAdmin = firstAdmin;
      });
  }

  fetchUsers(): void {
    this.clientService.getAll()
      .subscribe(users => {
        if (this.loggedUser.role === 'ADMIN') {
          this.users = users;
          if (users.length > 0) {
            this.selectUser(users[0]);
          }
        } else {
          this.selectedUser = this.firstAdmin;
        }
        this.connectWebSocket();
      });
  }

  selectUser(user: ClientModel): void {
    this.selectedUser = user;
    this.messages = [];
    this.connectWebSocket();
  }

  sendMessage(toUserId: string | undefined, status: string): void {
    if (this.message.trim()) {
      const messageToBeSent: MessageModel = {
        toUserId: toUserId!,
        fromUserId: this.loggedUser.id,
        message: this.message,
        status: status
      };
      this.socket?.send(JSON.stringify(messageToBeSent));
      this.messages.push({text: this.message, sent: true, read: false});
      this.message = '';
      this.isStatusTyping = false;
      this.clearTypingTimeout();
      this.scrollToBottom();
    }
  }

  private handleIncomingMessage(message: { message: string; status: string }) {
    if (message.status === 'read') {
      const lastSentMessage = this.messages.find(m => m.sent && !m.read);
      if (lastSentMessage) {
        lastSentMessage.read = true;
      }
      return;
    } else if (message.status === 'typing') {
      this.isStatusTyping = true;
      this.lastTypingTime = Date.now();
      this.clearTypingTimeout();
      return;
    }
    this.messages.push({text: message.message, sent: false});
    this.scrollToBottom();
  }

  private scrollToBottom(): void {
    try {
      const container = this.chatDisplay.nativeElement;
      container.scrollTop = container.scrollHeight;
    } catch (error) {
      console.error("Auto-scroll error:", error);
    }
  }

  onScroll(): void {
    const container = this.chatDisplay.nativeElement;
    this.userAtBottom = container.scrollHeight - container.scrollTop <= container.clientHeight;
  }

  onTyping(): void {
    if (this.selectedUser && this.socket) {
      if (this.message.trim() !== '') {
        if (Date.now() - this.lastTypingTime > 1000) {
          this.socket.send(JSON.stringify({
            toUserId: this.selectedUser!.id,
            fromUserId: this.loggedUser.id,
            message: '',
            status: 'typing'
          }));
          this.lastTypingTime = Date.now();
        }
      } else {
        this.isStatusTyping = false;
        this.clearTypingTimeout();
      }
    }
  }

  onClearMessage(): void {
    if (this.message.trim() === '') {
      this.isStatusTyping = false;
      this.clearTypingTimeout();
    }
  }

  onFocus(): void {
    if (this.socket && this.selectedUser) {
      this.socket.send(JSON.stringify({
        toUserId: this.selectedUser!.id,
        fromUserId: this.loggedUser.id,
        message: '',
        status: 'read'
      }));
    }
  }

  private clearTypingTimeout(): void {
    clearTimeout(this.typingTimeout);
    this.typingTimeout = setTimeout(() => {
      this.isStatusTyping = false;
    }, 2000);
  }
}
