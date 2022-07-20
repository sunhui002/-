package com.example.auth.Entity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class DeleteAttrsVo {

    public int spuid;

   public Set<Integer> deletedskuid;

   public Map<Integer, List<String>> deleteattrvaluemap;

    public DeleteAttrsVo() {
    }

    public int getSpuid() {
        return spuid;
    }

    public void setSpuid(int spuid) {
        this.spuid = spuid;
    }

    public Set<Integer> getDeletedskuid() {
        return deletedskuid;
    }

    public void setDeletedskuid(Set<Integer> deletedskuid) {
        this.deletedskuid = deletedskuid;
    }

    public Map<Integer, List<String>> getDeleteattrvaluemap() {
        return deleteattrvaluemap;
    }

    public void setDeleteattrvaluemap(Map<Integer, List<String>> deleteattrvaluemap) {
        this.deleteattrvaluemap = deleteattrvaluemap;
    }
}
