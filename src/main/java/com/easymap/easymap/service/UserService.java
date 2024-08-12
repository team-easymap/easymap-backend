package com.easymap.easymap.service;

import com.easymap.easymap.dto.request.user.UserNicknameDuplicateRequestDTO;
import com.easymap.easymap.dto.request.user.UserRequiredInfoRequestDto;
import com.easymap.easymap.entity.User;

import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    boolean userNicknameDuplicateCheck(UserNicknameDuplicateRequestDTO userNicknameDuplicateRequestDTO);
    List<String> userAdditionalInfoCheck();


    void userWithdraw(UserDetails userDetails);

    boolean patchUserRequiredInfo(UserRequiredInfoRequestDto userInfo);

}
