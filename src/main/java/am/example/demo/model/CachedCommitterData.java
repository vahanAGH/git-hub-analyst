package am.example.demo.model;

import java.util.Collection;

public class CachedCommitterData {

  public Collection<Committer> committerS;
  public String nextUrl;

  public CachedCommitterData(Collection<Committer> committerS) {
    this.committerS = committerS;
  }

  public CachedCommitterData(Collection<Committer> committerS, String nextUrl) {
    this.committerS = committerS;
    this.nextUrl = nextUrl;
  }
}
