package com.example.pifinance_back.Services;

import com.example.pifinance_back.Entities.Vote;

public interface VoteService {
    Vote voteUserlike(int idevent, int iduser);
    Vote voteUserdislike(int idevent,int iduser);
}
