package com.easymap.easymap.service;

import com.easymap.easymap.dto.request.user.UserNicknameDuplicateRequestDTO;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    boolean userNicknameDuplicateCheck(UserNicknameDuplicateRequestDTO userNicknameDuplicateRequestDTO);

    void userWithdraw(UserDetails userDetails);
}
