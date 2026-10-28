package com.college.ccms.web;

import com.college.ccms.model.ComplaintStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** One row on the dashboard: a status name and how many complaints have it. */
public class StatusCount {

    private final String key;
    private final String label;
    private final long count;

    public StatusCount(ComplaintStatus status, long count) {
        this.key = status.name();
        this.label = status.getLabel();
        this.count = count;
    }

    public static List<StatusCount> list(Map<ComplaintStatus, Long> counts) {
        List<StatusCount> rows = new ArrayList<StatusCount>();
        for (ComplaintStatus status : ComplaintStatus.values()) {
            Long value = counts.get(status);
            rows.add(new StatusCount(status, value == null ? 0L : value.longValue()));
        }
        return rows;
    }

    public String getKey() {
        return key;
    }

    public String getLabel() {
        return label;
    }

    public long getCount() {
        return count;
    }
}
