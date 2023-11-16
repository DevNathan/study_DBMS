package view;

import java.util.List;
import java.util.Scanner;

import dao.PostDAO;
import dao.ReplyDAO;
import vo.PostDTO;
import vo.PostVO;
import vo.ReplyDTO;
import vo.ReplyVO;

public class ViewPost {
	PostDAO postDAO = new PostDAO();
	ReplyDAO replyDAO = new ReplyDAO();
	ReplyVO replyVO = new ReplyVO();
	PostVO postVO = new PostVO();
	
	public void createPost() {
		Scanner sc = new Scanner(System.in);
		System.out.println("게시글의 제목을 입력해주세요.");
		postVO.setPostTitle(sc.nextLine());

		System.out.println("게시글의 내용을 입력해주세요.");
		postVO.setPostContent(sc.nextLine());

		postDAO.post(postVO);
	}

	public void viewPostsAll() {
		List<PostDTO> posts = postDAO.getListWithReplyCount();
		posts.stream().forEach(post -> System.out
				.println("ID : " + post.getId() + "\t" + post.getPostTitle() + "\t" + 
						"작성자 : " + post.getMemberName() + "\t댓글수 : "+post.getReplyCount()));
	}

	public void viewPost(Long ID) {
		Scanner sc = new Scanner(System.in);
		char menuController = 0;

		System.out.println();
		PostDTO post = postDAO.findById(ID);
		System.out.println(post.getPostTitle() + "\n" + post.getPostContent() + "\n" + post.getMemberName());
		while (menuController != 'q') {
			System.out.println("1. 댓글 보기 | 2. 댓글 작성 | 3. 댓글 수정 | 4. 댓글 삭제 | 5. 게시글 수정 | 6. 게시글 삭제 | q. 뒤로가기");
			menuController = sc.next().charAt(0);
			sc.nextLine(); // 개행 문자 제거

			switch (menuController) {
			case '1':
				List<ReplyDTO> replies = replyDAO.viewAllReplies();
				if (replies.isEmpty()) {
					System.out.println("댓글이 없습니다.");
				} else {
					replies.stream().forEach(reply -> System.out.println(reply.getId() + "\t" + reply.getMemberName() + 
									"\t" + reply.getReplyContent()));
				}
				break;
			case '2':
				ReplyVO replyVO = new ReplyVO();

				System.out.print("댓글 입력 : ");
				String replyContent = sc.nextLine();
				if (replyContent.trim().isEmpty()) {
					System.out.println("댓글 내용을 입력하세요.");
				} else {
					replyVO.setReplyContent(replyContent);
					replyVO.setPostId(ID); // 현재 게시글의 ID를 댓글에 설정
					replyDAO.reply(replyVO);
					System.out.println("댓글이 작성되었습니다.");
				}
				break;
			case '3':
			    String check = "";

			    System.out.println("수정할 댓글의 번호를 입력해주세요.");
			    check = sc.next();
			    sc.nextLine();

			    try {
			        if (replyDAO.checkMemberId(Long.parseLong(check))) {
			            System.out.println("댓글을 입력해주세요.");
			            String updatedReply = sc.nextLine();
			            replyDAO.updateReply(updatedReply, Long.parseLong(check));
			        } else {
			            System.out.println("수정 권한이 없습니다.");
			        }
			    } catch (NumberFormatException e) {
			        System.out.println("잘못된 값을 입력했습니다.");
			    }
			    break;
			case '5':
				if (postDAO.checkMemberId() == true) {
					System.out.println("게시글의 제목을 입력해주세요.");
					postVO.setPostTitle(sc.nextLine());

					System.out.println("게시글의 내용을 입력해주세요.");
					postVO.setPostContent(sc.nextLine());

					postDAO.postUpdate(postVO);
				} else {
					System.out.println("수정 권한이 없습니다.");
				}
				break;
			case 'q':
				return;
			default:
				System.out.println("잘못 입력하셨습니다.");
			}
			System.out.println();
		}
	}
}
