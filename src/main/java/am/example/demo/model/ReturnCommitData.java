package am.example.demo.model;

import am.example.demo.utils.PageOrder;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ReturnCommitData implements Serializable {

  public Map<PageOrder, String> pagination;
  public List<Commit> commitList;

  public ReturnCommitData(List<Commit> commitList) {
    this.commitList = commitList;
  }

  public ReturnCommitData(List<Commit> commitList, Map<PageOrder, String> pagination) {
    this.commitList = commitList;
    this.pagination = pagination;
  }
}
