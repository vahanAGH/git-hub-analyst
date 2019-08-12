package am.example.demo.model;

import java.util.Collection;

public class ReturnCommitterData {

  public Collection<Committer> committerList;
  public boolean isShownAdditionalPageLink;

  public ReturnCommitterData(Collection<Committer> committerList) {
    this.committerList = committerList;
  }

  public ReturnCommitterData(Collection<Committer> committerList, boolean isShownAdditionalPageLink) {
    this.committerList = committerList;
    this.isShownAdditionalPageLink = isShownAdditionalPageLink;
  }
}
