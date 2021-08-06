package com.projectTeam.therapist.restService;

import com.projectTeam.therapist.boardService.BoardServiceImpl;
import com.projectTeam.therapist.model.ReplyDto;
import com.projectTeam.therapist.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReplyApiController {
    @Autowired
    private ReplyRepository replyRepository;
    private BoardServiceImpl boardServiceImpl;
    @Autowired
    public ReplyApiController(BoardServiceImpl boardServiceImpl) {
        this.boardServiceImpl = boardServiceImpl;
    }

    // postId에 따른 답글 조회
    // TODO : 게시글에 달린 답글이 몇개인지 조회하는 것 추가(https://www.notion.so/API-f4ce3713b77e4117822d298ef2b204c4)
    @GetMapping("/replies/{postId}")
    List<ReplyDto> findReplies(@PathVariable Long postId) {
        return replyRepository.findByPostId(postId);
    }

    // 답글 생성
    @PostMapping("/replies/{postId}")
    ReplyDto newReply(@RequestBody ReplyDto newReply, @PathVariable Long postId) {
        newReply.setPostId(postId);
        newReply.setStar(0);
        return replyRepository.save(newReply);
    }

    // 답글 수정
    @PutMapping("replies/{replyId}")
    ReplyDto modifyReply(@RequestBody() ReplyDto modifiedReply,
                         @PathVariable Long replyId) {
        return replyRepository.findById(replyId)
                .map(replyDto -> {
                    replyDto.setReplyContent(modifiedReply.getReplyContent());
                    return replyRepository.save(replyDto);
                })
                .orElseGet(() -> {
                    modifiedReply.setReplyId(replyId);
                    return replyRepository.save(modifiedReply);
                });
    }

    // 답글 삭제
    @DeleteMapping("/replies/{replyId}")
    void deleteReply(@PathVariable Long replyId) {
        replyRepository.deleteById(replyId);
    }
}