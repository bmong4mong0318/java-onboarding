import java.util.List;

class PlayerPro1 {

  public final int score;

  public PlayerPro1(int left, int right) {
    PagePro1 page = new PagePro1(left, right);
    score = page.getScore();
  }

  public int getScore() {
    return this.score;
  }

}
class Problem1 {

  public static int solution(List<Integer> pobi, List<Integer> crong) {
    if (pobi.size() != 2 || crong.size() != 2 ||
        PagePro1.isValidatePage(pobi.get(0), pobi.get(1)) ||
        PagePro1.isValidatePage(crong.get(0), crong.get(1))) {
      return -1;
    }
    return ScorePro1.compareScore(new PlayerPro1(pobi.get(0), pobi.get(1)).getScore(),
        new PlayerPro1(crong.get(0), crong.get(1)).getScore());
  }
}