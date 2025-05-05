package bsuir.backend.bot.service;

import bsuir.backend.bot.repo.UserRepository;

public class UserAccessService {

    private final UserRepository userRepository;

    public UserAccessService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isAuthorized(Long telegramId){return  userRepository.existsByTelegramId(telegramId);}


}
