package pro.sayapker.repository.jdbcClient.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import pro.sayapker.dto.PaginationResponse;
import pro.sayapker.dto.horse.ClientRequest;
import pro.sayapker.dto.horse.HorseResponse;
import pro.sayapker.repository.jdbcClient.HorseJDBC;
import java.math.BigDecimal;
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
        List<HorseResponse> responseList = jdbcClient.sql(findAllHorses)
                .param(pageSize)
                .param(offset).query((rs, rsnNum) ->
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
        return new PaginationResponse<>(
                pageNumber,
                pageSize,
                totalElement,
                totalPages,
                responseList
        );
    }

    @Override
    public PaginationResponse<HorseResponse> getAllBookForClient(ClientRequest clientRequest, int pageNumber, int pageSize) {
        int offset = (pageNumber - 1) * pageSize;

        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sql = new StringBuilder("""
                       SELECT h.id AS horseId,
                       h.name AS horseName,
                       u.image_url AS ownerImage,
                       CONCAT(u.first_name, ' ', u.last_name) AS ownerName,
                       h.birth_date AS dataOfBirthday,
                       h.price AS price,
                       STRING_AGG(hi.images, ', ') AS images,
                       (SELECT COUNT(*) FROM likes l WHERE l.horse_id = h.id) AS countLike
                FROM horses h
                         JOIN users u ON u.id = h.user_id
                         LEFT JOIN horse_images hi ON hi.horse_id = h.id
                WHERE h.status = 'ACCEPTED'
                """
        );


        if (clientRequest.getName() != null && !clientRequest.getName().isEmpty()) {
            sql.append(" AND h.name ILIKE :name");
            params.addValue("name", "%" + clientRequest.getName() + "%");
        }

        if (clientRequest.getBirthDate() != null) {
            sql.append(" AND DATE(h.birth_date) = :birthDate");
            params.addValue("birthDate", clientRequest.getBirthDate());
        }

        if (clientRequest.getHomeland() != null && !clientRequest.getHomeland().isEmpty()) {
            sql.append(" AND h.homeland ILIKE :homeland");
            params.addValue("homeland", "%" + clientRequest.getHomeland() + "%");
        }

        if (clientRequest.getStartPrice() > 0) {
            sql.append(" AND h.price >= :startPrice");
            params.addValue("startPrice", BigDecimal.valueOf(clientRequest.getStartPrice()));
        }

        if (clientRequest.getEndPrice() > 0) {
            sql.append(" AND h.price <= :endPrice");
            params.addValue("endPrice", BigDecimal.valueOf(clientRequest.getEndPrice()));
        }

        sql.append(" GROUP BY h.id, h.name, u.image_url, u.first_name, u.last_name, h.birth_date, h.price");
        sql.append(" ORDER BY h.id");
        sql.append(" LIMIT :pageSize OFFSET :offset");

        params.addValue("pageSize", pageSize);
        params.addValue("offset", offset);

        List<HorseResponse> horses = jdbcClient.sql(sql.toString())
                .paramSource(params)
                .query((rs, rowNum) -> HorseResponse.builder()
                        .horseId(rs.getLong("horseId"))
                        .horseName(rs.getString("horseName"))
                        .ownerImage(rs.getString("ownerImage"))
                        .ownerName(rs.getString("ownerName"))
                        .dataOfBirthday(rs.getTimestamp("dataOfBirthday"))
                        .price(rs.getBigDecimal("price"))
                        .image(rs.getString("images") != null ? rs.getString("images").split(", ")[0] : null)
                        .countLike(rs.getInt("countLike"))
                        .build())
                .list();

        StringBuilder countSql = new StringBuilder(
                "SELECT COUNT(DISTINCT h.id) " +
                        "FROM horses h " +
                        "JOIN users u ON u.id = h.user_id " +
                        "WHERE h.status = 'ACCEPTED'"
        );

        if (clientRequest.getName() != null && !clientRequest.getName().isEmpty()) {
            countSql.append(" AND h.name ILIKE :name");
        }

        if (clientRequest.getBirthDate() != null) {
            countSql.append(" AND DATE(h.birth_date) = :birthDate");
        }

        if (clientRequest.getHomeland() != null && !clientRequest.getHomeland().isEmpty()) {
            countSql.append(" AND h.homeland ILIKE :homeland");
        }

        if (clientRequest.getStartPrice() > 0) {
            countSql.append(" AND h.price >= :startPrice");
        }

        if (clientRequest.getEndPrice() > 0) {
            countSql.append(" AND h.price <= :endPrice");
        }

        Long totalElements = jdbcClient.sql(countSql.toString())
                .paramSource(params)
                .query((rs, rowNum) -> rs.getLong(1))
                .single();

        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        return PaginationResponse.<HorseResponse>builder()
                .content(horses)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .build();
    }
}
