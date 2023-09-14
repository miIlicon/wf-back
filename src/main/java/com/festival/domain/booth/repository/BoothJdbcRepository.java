package com.festival.domain.booth.repository;

import com.festival.domain.booth.controller.dto.BulkInsertBooth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class BoothJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public void insertBoothList(List<BulkInsertBooth> bulkInsertBooths){

        jdbcTemplate.batchUpdate("insert into booth (created_by, created_date, last_modified_by, last_modified_date, status, content, latitude, longitude, " +
                        "sub_title, title, type, view_count, image_id, member_id) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, bulkInsertBooths.get(i).getCreatedBy());
                        ps.setTimestamp(2, java.sql.Timestamp.valueOf(bulkInsertBooths.get(i).getCreatedDate()));
                        ps.setString(3, bulkInsertBooths.get(i).getLastModifiedBy());
                        ps.setTimestamp(4, java.sql.Timestamp.valueOf(bulkInsertBooths.get(i).getLastModifiedDate()));
                        ps.setString(5, bulkInsertBooths.get(i).getStatus());
                        ps.setString(6, bulkInsertBooths.get(i).getContent());
                        ps.setFloat(7, bulkInsertBooths.get(i).getLatitude());
                        ps.setFloat(8, bulkInsertBooths.get(i).getLongitude());
                        ps.setString(9, bulkInsertBooths.get(i).getSubTitle());
                        ps.setString(10, bulkInsertBooths.get(i).getTitle());
                        ps.setString(11, bulkInsertBooths.get(i).getType());
                        ps.setLong(12, bulkInsertBooths.get(i).getViewCount());
                        ps.setLong(13, bulkInsertBooths.get(i).getImageId());
                        ps.setLong(14, bulkInsertBooths.get(i).getMemberId());
                    }

                    @Override
                    public int getBatchSize() {
                        return bulkInsertBooths.size();
                    }
                });
    }
}
