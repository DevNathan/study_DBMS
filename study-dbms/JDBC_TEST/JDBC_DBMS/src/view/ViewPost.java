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
		System.out.println("�Խñ��� ������ �Է����ּ���.");
		postVO.setPostTitle(sc.nextLine());

		System.out.println("�Խñ��� ������ �Է����ּ���.");
		postVO.setPostContent(sc.nextLine());

		postDAO.post(postVO);
	}

	public void viewPostsAll() {
		List<PostDTO> posts = postDAO.getListWithReplyCount();
		posts.stream().forEach(post -> System.out
				.println("ID : " + post.getId() + "\t" + post.getPostTitle() + "\t" + 
						"�ۼ��� : " + post.getMemberName() + "\t��ۼ� : "+post.getReplyCount()));
	}

	public void viewPost(Long ID) {
		Scanner sc = new Scanner(System.in);
		char menuController = 0;

		System.out.println();
		PostDTO post = postDAO.findById(ID);
		System.out.println(post.getPostTitle() + "\n" + post.getPostContent() + "\n" + post.getMemberName());
		while (menuController != 'q') {
			System.out.println("1. ��� ���� | 2. ��� �ۼ� | 3. ��� ���� | 4. ��� ���� | 5. �Խñ� ���� | 6. �Խñ� ���� | q. �ڷΰ���");
			menuController = sc.next().charAt(0);
			sc.nextLine(); // ���� ���� ����

			switch (menuController) {
			case '1':
				List<ReplyDTO> replies = replyDAO.viewAllReplies();
				if (replies.isEmpty()) {
					System.out.println("����� �����ϴ�.");
				} else {
					replies.stream().forEach(reply -> System.out.println(reply.getId() + "\t" + reply.getMemberName() + 
									"\t" + reply.getReplyContent()));
				}
				break;
			case '2':
				ReplyVO replyVO = new ReplyVO();

				System.out.print("��� �Է� : ");
				String replyContent = sc.nextLine();
				if (replyContent.trim().isEmpty()) {
					System.out.println("��� ������ �Է��ϼ���.");
				} else {
					replyVO.setReplyContent(replyContent);
					replyVO.setPostId(ID); // ���� �Խñ��� ID�� ��ۿ� ����
					replyDAO.reply(replyVO);
					System.out.println("����� �ۼ��Ǿ����ϴ�.");
				}
				break;
			case '3':
			    String check = "";

			    System.out.println("������ ����� ��ȣ�� �Է����ּ���.");
			    check = sc.next();
			    sc.nextLine();

			    try {
			        if (replyDAO.checkMemberId(Long.parseLong(check))) {
			            System.out.println("����� �Է����ּ���.");
			            String updatedReply = sc.nextLine();
			            replyDAO.updateReply(updatedReply, Long.parseLong(check));
			        } else {
			            System.out.println("���� ������ �����ϴ�.");
			        }
			    } catch (NumberFormatException e) {
			        System.out.println("�߸��� ���� �Է��߽��ϴ�.");
			    }
			    break;
			case '5':
				if (postDAO.checkMemberId() == true) {
					System.out.println("�Խñ��� ������ �Է����ּ���.");
					postVO.setPostTitle(sc.nextLine());

					System.out.println("�Խñ��� ������ �Է����ּ���.");
					postVO.setPostContent(sc.nextLine());

					postDAO.postUpdate(postVO);
				} else {
					System.out.println("���� ������ �����ϴ�.");
				}
				break;
			case 'q':
				return;
			default:
				System.out.println("�߸� �Է��ϼ̽��ϴ�.");
			}
			System.out.println();
		}
	}
}
