package pro.sayapker.repository.jdbcClient.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import pro.sayapker.dto.PaginationResponse;
import pro.sayapker.dto.horse.HorseResponse;
import pro.sayapker.repository.jdbcClient.HorseJDBC;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HorseJDBCImpl implements HorseJDBC {
   private final JdbcClient jdbcClient;
    @Override
    public PaginationResponse<HorseResponse> findAllHorse(int pageNumber, int pageSize) {
        int offset = (pageNumber - 1) * pageSize;
        String findAllHorses = """
                select
                    h.id as horseId,
                    h.name as horseName,
                    u.first_name ,
                    u.last_name,
                    u.image_url as ownerImage,
                    h.birth_date as dataOfBirthday,
                    (select hi.images from horse_images hi where hi.horse_id=h.id limit 1) as image,
                    (select count(l.id) from likes l where l.horse_id = h.id ) as count
                from horses h
                    join users u on u.id = h.user_id
                where h.status = 'ACCEPTED'
                limit ? offset ?
                """;
        List<HorseResponse> responseList = jdbcClient.sql(findAllHorses).param(pageNumber).param(offset).query((rs, rsnNum) ->
                HorseResponse.builder()
                        .horseId(rs.getLong("horseId"))
                        .horseName(rs.getString("horseName"))
                        .ownerImage(rs.getString("ownerImage"))
                        .dataOfBirthday(rs.getDate("dataOfBirthday"))
                        .image(rs.getString("image"))
                        .ownerName(rs.getString("first_name") + " " + rs.getString("last_name"))
                        .countLike(rs.getInt("count"))
                        .build()).list();
        String total = """
                select
                count(h.id)
                from horses h
                where h.status = 'ACCEPTED'
                """;
        Long totalElement = jdbcClient.sql(total).query(Long.class).single();
        int totalPages = (int) Math.ceil((double) totalElement / (double) pageSize);
        return  new PaginationResponse<>(
                pageNumber,
                pageSize,
                totalElement,
                totalPages,
                responseList
        );
    }
}
