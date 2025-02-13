package com.urlshortener.core.domain.shortener.repository;

import com.urlshortener.core.domain.shortener.dataTransferObject.DeviceAnalyticDTO;
import com.urlshortener.core.domain.shortener.dataTransferObject.OverTimeAnalyticDTO;
import com.urlshortener.core.domain.shortener.dataTransferObject.ReferrerAnalyticDTO;
import com.urlshortener.core.domain.shortener.model.UrlAnalytic;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface UrlAnalyticRepository extends JpaRepository<UrlAnalytic, String> {
    long countByUrlId(String urlId);

    @Query("select a.deviceType, count(a) as totalClicksAndScans " +
            "from UrlAnalytic a " +
            "left join Url u " +
            "on u.userId = :userId " +
            "group by a.deviceType " +
            "order by totalClicksAndScans desc")
    List<DeviceAnalyticDTO> findTopDeviceTypesByUserId(String userId, Pageable pageable);

    @Query("select a.deviceType, count(a) as totalClicksAndScans " +
            "from UrlAnalytic a " +
            "where a.urlId = :urlId " +
            "group by a.deviceType " +
            "order by totalClicksAndScans desc")
    List<DeviceAnalyticDTO> findTopDeviceTypesByUrlId(String urlId, Pageable pageable);

    @Query("select a.referrer, count(a) as totalClicksAndScans " +
            "from UrlAnalytic a " +
            "where a.urlId = :urlId " +
            "group by a.referrer " +
            "order by totalClicksAndScans desc ")
    List<ReferrerAnalyticDTO> findTopReferrersByUrlId(String urlId, Pageable pageable);

    @Query("select a.accessedAt, count(a) as totalClicksAndScans " +
            "from UrlAnalytic a " +
            "where a.urlId = :urlId and a.accessedAt between :startDate and :endDate " +
            "group by a.accessedAt " +
            "order by a.accessedAt desc")
    List<OverTimeAnalyticDTO> findOverTimeByUrlIdAndDateRange(
            String userId,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable);
}

