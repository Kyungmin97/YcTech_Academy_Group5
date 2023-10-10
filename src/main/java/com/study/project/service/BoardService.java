package com.study.project.service;

import com.study.project.entity.Board;
import com.study.project.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;
    public void write(Board board) {
        boardRepository.save(board);

    }

    public List<Board> boardList(){
        return boardRepository.findAll();
    }
}
