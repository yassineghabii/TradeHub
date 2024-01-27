import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PostService } from 'src/app/services/post.service';
import { TopicService } from 'src/app/services/topic.service';
import { CommentService } from 'src/app/services/comment.service';
import { faThumbsUp, faThumbsDown, faTrashAlt, faEdit, faComment, faCommentAlt, faCommentDots, faComments } from '@fortawesome/free-regular-svg-icons'
import { faBars, faListAlt, faThumbsUp as faThumbsUpp, faThumbsDown as faThumbsDownn } from '@fortawesome/free-solid-svg-icons'
import { Router } from '@angular/router';
import { ElementRef } from '@angular/core';
import { WindowRef } from 'src/app/services/window-ref.service';
import { forkJoin } from 'rxjs';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Post } from 'src/app/entities/Post'; 
import { AuthService } from 'src/app/services/authservices';
import { Comment } from 'src/app/entities/Comment';
import * as bootstrap from 'bootstrap';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Topic } from 'src/app/entities/Topic';


@Component({
  selector: 'app-topic-details',
  templateUrl: './topic-details.component.html',
  styleUrls: ['./topic-details.component.scss']
})
export class TopicDetailsComponent implements OnInit {

  modalReference: ElementRef;

    faThumbsUp = faThumbsUp;
    faThumbsUpp = faThumbsUpp;
    faThumbsDown = faThumbsDown;
    faThumbsDownn = faThumbsDownn;
    faTrashAlt = faTrashAlt;
    faEdit = faEdit;
    faComments = faComments;
    faBars = faBars;
    faListAlt = faListAlt;

    topicId: number;
    postId: number;

    topicDetails: any;
    postDetails: any;

    posts: any;
    coments: any;
    visibleComments: any;

    Postupdate: any;
    Commentupdate: any;

    currentPage = 1;
    pageSize = 5;


    postForm: FormGroup;
    Post:Post; 
    users: any;
    idUser: number;
    commentForm: FormGroup;
    Comment:Comment; 

    userVoteStatus1: string;
    userVoteStatus2: string;


    constructor(
      private route: ActivatedRoute,
      private formBuilder: FormBuilder,
      private topicService: TopicService,
      private postService: PostService,
      private UserService: AuthService,
      private commentService: CommentService,
      private router: Router,
      private el: ElementRef,
      private windowRef: WindowRef,
      private modalService: NgbModal
    ) {}
  
    ngOnInit(): void {
      this.route.params.subscribe(params => {
        this.topicId = +params['id'];
        console.log('Topic ID:', this.topicId);
        this.getTopicDetails();
        this.getPostsByTopic();
        this.getPostDetails();

      });

      this.UserService.getAllUsers().subscribe(data => {
        this.users = data;
      });

      this.postForm = this.formBuilder.group({
        content: [''],
        user: [''],
        topic: ['']    
      });

      this.Post = {
        id_post: null,
        content: null,
        likes: null,
        dislikes: null,
        creation_date: null,
        modified: null,
        user_id: null,
        user: null,
        topic: null,
        comments: null
     }

      this.Postupdate = {
        id_post: null,
        content: null,
        likes: null,
        dislikes: null,
        creation_date: null,
        modified: null,
        user: null,
        topic: null,
        comments: null
     }

     this.commentForm = this.formBuilder.group({
      content: [''],
      user: [''],
      post: ['']    
    });

    this.Comment = {
      id_comment: null,
      content: null,
      likes: null,
      dislikes: null,
      creation_date: null,
      modified: null,
      user_id: null,
      user: null,
      post: null
   }

     this.Commentupdate = {
      id_comment: null,
      content: null,
      likes: null,
      dislikes: null,
      creation_date: null,
      modified: null,
      user: null,
      post: null
   }

    }

    isCurrentUserOwner(topic: Topic): boolean {
      const userId = this.getUserId();
      return userId !== null && topic.user_id === userId;
    }

    isCurrentUserOwner1(post: Post): boolean {
      const userId = this.getUserId();
      return userId !== null && post.user_id === userId;
    }

    isCurrentUserOwner2(comment: Comment): boolean {
      const userId = this.getUserId();
      return userId !== null && comment.user_id === userId;
    }
  
    getTopicDetails() {
      this.topicService.getTopic(this.topicId).subscribe(res => {
        this.topicDetails = res;
      });
    }

    getPostDetails() {
      this.postService.getPost(this.postId).subscribe(res => {
        this.postDetails = res;
      });
    }
  
    getPostsByTopic() {
      this.postService.getPostsByTopicId(this.topicId).subscribe(res => {
        this.posts = res;

        this.posts.forEach((post: any) => {

          this.postService.commentCount(post.id_post).subscribe((count: number) => {
            post.commentCount = count;
          });

          this.postService.getUserVoteStatus1(post.id_post, this.getUserId()).subscribe((userVoteStatus1: string) => {
            post.userVoteStatus1 = userVoteStatus1;
        });

        });
      });
    }

    getCommentsByPost1() {
      this.commentService.getCommentsByPostId(this.postId).subscribe(res => {
        this.coments = res;

        this.coments.forEach((comment: any) => {

          this.commentService.getUserVoteStatus2(comment.id_comment, this.getUserId()).subscribe((userVoteStatus2: string) => {
            comment.userVoteStatus2 = userVoteStatus2;
        });

        });

      });
    }

    getCommentsByPost(postId: number) {
      this.commentService.getCommentsByPostId(postId).subscribe(res => {
        this.coments = res;

        this.coments.forEach((comment: any) => {

          this.commentService.getUserVoteStatus2(comment.id_comment, this.getUserId()).subscribe((userVoteStatus2: string) => {
            comment.userVoteStatus2 = userVoteStatus2;
        });

        });

      });
    }




    deleteTopic(IdTopic:number){
      this.topicService.deleteTopic(IdTopic).subscribe(()=>{
        this.router.navigate(['/topic']);
      });
       }

       confirmerSuppression2(idTopic: number) {
        const estConfirme = this.windowRef.nativeWindow.confirm("Are you sure you want to delete this topic?");
        if (estConfirme) {
          this.deleteTopic(idTopic);
        }
      }


       

      votelikeP(idPost: any) {

        const userId = this.getUserId();

        this.postService.voteLike(idPost, userId).subscribe(
          (resp) => {
            console.log('Post liked successfully:', resp);
            this.getPostsByTopic();
          },
          (err) => {
            console.error('Error while liking Post:', err);
          }
        );
      }

      votedislikeP(idPost: any) {

        const userId = this.getUserId();

        this.postService.voteDislike(idPost, userId).subscribe(
          (resp) => {
            console.log('Post disliked successfully:', resp);
            this.getPostsByTopic();
          },
          (err) => {
            console.error('Error while disliking Post:', err);
          }
        );
      }


      votelikeC(idComment: any) {

        const userId = this.getUserId();

        this.commentService.voteLike(idComment, userId).subscribe(
          (resp) => {
            console.log('Comment liked successfully:', resp);
            this.getCommentsByPost1();
          },
          (err) => {
            console.error('Error while liking Comment:', err);
          }
        );
      }

      votedislikeC(idComment: any) {

        const userId = this.getUserId();

        this.commentService.voteDislike(idComment, userId).subscribe(
          (resp) => {
            console.log('Comment disliked successfully:', resp);
            this.getCommentsByPost1();
          },
          (err) => {
            console.error('Error while disliking Comment:', err);
          }
        );
      }

      
      addPost() {
        if (this.postForm.invalid) {
            console.log('Invalid form');
            return;
        }
    
        const userId = this.getUserId();
        const topicId = this.topicId;

        this.postService.addPost(this.postForm.value, userId, topicId).subscribe(
            (resp) => {
                console.log('Post added successfully:', resp);
                this.modalService.dismissAll();
                this.getPostsByTopic();           
               },
            (err) => {
                console.log('Error while adding post:', err);
            }
        );
    }

    addComment() {
      if (this.commentForm.invalid) {
          console.log('Invalid form');
          return;
      }
  
      const userId = this.getUserId();
      const postId = this.postId;

      this.commentService.addComment(this.commentForm.value, userId, postId).subscribe(
          (resp) => {
              console.log('Comment added successfully:', resp);
              window.location.reload();
          },
          (err) => {
              console.log('Error while adding comment:', err);
          }
      );
  }

     edit(post: any, content: any){
       this.Postupdate = post;

       this.modalService.open(content, { ariaLabelledBy: 'commentModalLabel' });
     }

     add(post: any){
      this.Post = post;
    }

     edit1(comment: any, content: any){
      this.Commentupdate = comment;

      this.modalService.open(content, { ariaLabelledBy: 'commentModalLabel' });
    }

    add1(comment: any){
      this.Comment = comment;
    }

    updatePost(){
     this.postService.updatePost(this.Postupdate, this.Postupdate.id_post).subscribe(
       (resp) => {
         console.log('Post updated successfully:', resp);
         this.modalService.dismissAll();
         this.getPostsByTopic();
       },
       (err) => {
         console.log('Error while updating post:', err);
       }
     );}

     updateComment(){
      this.commentService.updateComment(this.Commentupdate, this.Commentupdate.id_comment).subscribe(
        (resp) => {
          console.log('Comment updated successfully:', resp);
          this.getCommentsByPost1();
          window.location.reload();
        },
        (err) => {
          console.log('Error while updating comment:', err);
        }
      );}

     deletePost(IdPost:number){
      this.postService.deletePost(IdPost).subscribe(()=>{
        this.modalService.dismissAll();
        this.getPostsByTopic();
        
      }, (err) => {
        console.error('Error while deleting post:', err);
      });
       }

       deleteComment(IdComment:number){
        this.commentService.deleteComment(IdComment).subscribe(()=>{
          this.getCommentsByPost1();
          window.location.reload();
        }, (err) => {
          console.error('Error while deleting comment:', err);
        });
         }

        
         confirmerSuppression(idPost: number) {
          const estConfirme = this.windowRef.nativeWindow.confirm("Are you sure you want to delete this post?");
          if (estConfirme) {
            this.deletePost(idPost);
          }
        }

        confirmerSuppression1(IdComment: number) {
          const estConfirme = this.windowRef.nativeWindow.confirm("Are you sure you want to delete this coment?");
          if (estConfirme) {
            this.deleteComment(IdComment);
          }
        }
        
      
        getUserId(): number | null {
          const userIdStr = sessionStorage.getItem('id');
          return userIdStr ? Number(userIdStr) : null;
        }
  
        openModal1(content: any) {
          this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' });
        }
        openModal2(content: any) {
          this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' });
        }

        
        openModal4(content: any) {
          this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' });
        }

        openModal6(content: any) {
          this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' });
        }
        

        openCommentModal(content: any) {
          this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' }); 
        }
      
       
        showCommentsModal(post: any, content: any) {
          console.log(post.id_post);
          if (post.id_post) {
            this.postId = post.id_post; // Assurez-vous de définir postId ici
            this.getCommentsByPost(post.id_post);
    
            this.postService.commentCount(post.id_post).subscribe((count: number) => {
              this.visibleComments = count;
            });

            this.modalService.open(content, { ariaLabelledBy: 'commentModalLabel' });
          }
        }

        
  }