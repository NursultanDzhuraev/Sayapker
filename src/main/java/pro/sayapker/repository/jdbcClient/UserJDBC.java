package pro.sayapker.repository.jdbcClient;

import pro.sayapker.dto.SimpleResponse;

public interface UserJDBC {
    SimpleResponse deleteUser(Long userId);
}
