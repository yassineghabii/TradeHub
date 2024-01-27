import { Post } from "./Post";
import { Client } from "./Client";

export class Comment {
    id_comment: any;
    content: any;
    likes: any;
    dislikes: any;
    creation_date: Date;
    modified: any;
    user_id: any;
    user: Client;
    post: Post;
  }
  