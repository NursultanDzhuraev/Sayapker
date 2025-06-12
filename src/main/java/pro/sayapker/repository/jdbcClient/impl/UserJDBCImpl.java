package pro.sayapker.repository.jdbcClient.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import pro.sayapker.dto.SimpleResponse;
import pro.sayapker.repository.jdbcClient.UserJDBC;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserJDBCImpl implements UserJDBC {
    private final JdbcClient jdbcClient;
    @Override
    public SimpleResponse deleteUser(Long userId) {
        List<Long> horseIds = jdbcClient.sql("select h.id from horses h join users u on u.id = h.user_id where u.id = ?")
                .param(userId).query(Long.class).list();
        for (Long horseId : horseIds) {
            String deleteLikesSql = "DELETE FROM likes l WHERE l.horse_id = ?";
            jdbcClient.sql(deleteLikesSql).param(horseId).update();
            String deleteAncestorsSql = "DELETE FROM horse_ancestors a WHERE a.horse_id = ?";
            jdbcClient.sql(deleteAncestorsSql).param(horseId).update();
            String deleteImagesSql = "DELETE FROM horse_images a WHERE a.horse_id = ?";
            jdbcClient.sql(deleteImagesSql).param(horseId).update();
            String sql = "DELETE FROM horses h WHERE h.id = ?";
            jdbcClient.sql(sql).param(horseId).update();
        }
        String deleteLikesSql = "DELETE FROM likes l WHERE l.user_id = ?";
        jdbcClient.sql(deleteLikesSql).param(userId).update();
        String deleteUsersSql = "DELETE FROM users u WHERE u.id = ?";
        int i = jdbcClient.sql(deleteUsersSql).param(userId).update();
        if (i > 0) {
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("succesfully deleted")
                    .build();
        }
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .message("not deleted")
                .build();
    }
}
