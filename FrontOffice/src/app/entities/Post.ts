import { Topic } from "./Topic";
import { Client } from "./Client";

export class Post {
    id_post: any;
    content: any;
    likes: any;
    dislikes: any;
    creation_date: Date;
    modified: any;
    user_id: any;
    user: Client;
    topic: Topic;
    comments: Comment[];
  }
  