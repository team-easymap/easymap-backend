package com.easymap.easymap.service;

import com.easymap.easymap.dto.request.user.UserNicknameDuplicateRequestDTO;

public interface UserService {
    boolean userNicknameDuplicateCheck(UserNicknameDuplicateRequestDTO userNicknameDuplicateRequestDTO);
}
