import { Post } from "./Post";
import { Client } from "./Client";

export class Topic {
    id_topic: any;
    title: any;
    question: any;
    likes: any;
    dislikes: any;
    creation_date: Date;
    user_id: any;
    user: Client;
    posts: Post[];
  }
  
  