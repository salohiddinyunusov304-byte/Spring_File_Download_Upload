package uz.pdp.dao;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import uz.pdp.entity.Upload;

@Component
public class UploadDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UploadDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void save(Upload upload) {
        String sql = "insert into uploads(originalFilename, generateFilename, mimeType, size) values(:originalFilename, :generateFilename, :mimeType, :size);";

        var parameterSource = new MapSqlParameterSource()
                .addValue("originalFilename", upload.getOriginalFilename())
                .addValue("generateFilename", upload.getGenerateFilename())
                .addValue("mimeType", upload.getMimeType())
                .addValue("size", upload.getSize());

        namedParameterJdbcTemplate.update(sql, parameterSource);
    }

    public Upload findByGeneratedFileName(String generatedFileName) {
        String sql = "select id, originalFilename,  generateFilename, mimeType, size from uploads where generateFilename = :generateFilename;";
        var parameterSource = new MapSqlParameterSource()
                .addValue("generateFilename", generatedFileName);

        return namedParameterJdbcTemplate.queryForObject(sql, parameterSource,
                (rs, rowNum) -> Upload.builder()
                        .id(rs.getInt("id"))
                        .originalFilename(rs.getString("originalFilename"))
                        .generateFilename(rs.getString("generateFilename"))
                        .mimeType(rs.getString("mimeType"))
                        .size(rs.getLong("size"))
                        .build());
    }
}
