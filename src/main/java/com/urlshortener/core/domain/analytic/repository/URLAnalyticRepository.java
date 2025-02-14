package com.urlshortener.core.domain.analytic.repository;

import com.urlshortener.core.domain.analytic.dataTransferObject.DeviceAnalyticDTO;
import com.urlshortener.core.domain.analytic.dataTransferObject.OverTimeAnalyticDTO;
import com.urlshortener.core.domain.analytic.dataTransferObject.ReferrerAnalyticDTO;
import com.urlshortener.core.domain.analytic.model.URLAnalytic;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface URLAnalyticRepository extends JpaRepository<URLAnalytic, String> {
    long countByUrlId(String urlId);

    @Query("select count(a.id) " +
            "from URLAnalytic a " +
            "left join URL u " +
            "on a.urlId = u.id " +
            "where u.userId = :userId")
    long countByUserId(String userId);

    @Query("select a.deviceType, count(a) as totalClicksAndScans " +
            "from URLAnalytic a " +
            "where a.urlId = :urlId " +
            "group by a.deviceType " +
            "order by totalClicksAndScans desc")
    List<DeviceAnalyticDTO> findTopDeviceTypesByUrlId(String urlId, Pageable pageable);

    @Query("select a.referrer, count(a) as totalClicksAndScans " +
            "from URLAnalytic a " +
            "where a.urlId = :urlId " +
            "group by a.referrer " +
            "order by totalClicksAndScans desc ")
    List<ReferrerAnalyticDTO> findTopReferrersByUrlId(String urlId, Pageable pageable);

    @Query("select a.accessedAt, count(a) as totalClicksAndScans " +
            "from URLAnalytic a " +
            "where a.urlId = :urlId and a.accessedAt between :startDate and :endDate " +
            "group by a.accessedAt " +
            "order by a.accessedAt desc")
    List<OverTimeAnalyticDTO> findOverTimeByUrlIdAndDateRange(
            String userId,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );
}

