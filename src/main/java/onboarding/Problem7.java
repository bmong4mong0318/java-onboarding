package onboarding;

import java.util.*;
import java.util.stream.Collectors;

class ValidatorPro7 {

  public final static int ID_A = 0;
  public final static int ID_B = 1;
  public final static int MIN_USER_LEN = 1;
  public final static int MAX_USER_LEN = 30;
  public final static int MIN_FRIENDS_SIZE = 1;
  public final static int MAX_FRIENDS_SIZE = 10_000;
  public final static int FRIENDS_LIST_SIZE = 2;
  public final static int MIN_VISITORS_SIZE = 1;
  public final static int MAX_VISITORS_SIZE = 10_000;
  public final static int MIN_ID_LEN = 1;
  public final static int MAX_ID_LEN = 30;

  public ValidatorPro7(String user, List<List<String>> friends, List<String> visitors) {
    validateUser(user);
    validateFriends(friends);
    validateVisitors(visitors);
  }

  public static void validateUser(String user) {
    if (user.length() < MIN_USER_LEN || user.length() > MAX_USER_LEN) {
      throw new IllegalArgumentException("user는 길이가 1 이상 30 이하인 문자열이다.");
    }
  }

  public static void validateFriends(List<List<String>> friends) {
    if (friends.size() < MIN_FRIENDS_SIZE || friends.size() > MAX_FRIENDS_SIZE) {
      throw new IllegalArgumentException("user는 길이가 1 이상 30 이하인 문자열이다.");
    }
    for (List<String> pair : friends) {
      if (pair.size() != FRIENDS_LIST_SIZE) {
        throw new IllegalArgumentException("friends의 각 원소는 길이가 2인");
      }
      if (pair.get(ID_A).equals(pair.get(ID_B))) {
        throw new IllegalArgumentException("동일한 친구 관계가 중복해서 주어지지 않는다");
      }
      validateId(pair.get(ID_A));
      validateId(pair.get(ID_B));
    }
  }

  public static void validateId(String id) {
    if (!id.equals(id.toLowerCase())) {
      throw new IllegalArgumentException("사용자 아이디는 알파벳 소문자로만 이루어져 있다");
    }
    if (id.length() < MIN_ID_LEN || id.length() > MAX_ID_LEN) {
      throw new IllegalArgumentException("아이디는 길이가 1 이상 30 이하인 문자열이다.");
    }
  }

  public static void validateVisitors(List<String> visitors) {
    if (visitors.size() < MIN_VISITORS_SIZE || visitors.size() > MAX_VISITORS_SIZE) {
      throw new IllegalArgumentException("visitors는 길이가 0 이상 10,000 이하인 리스트/배열이다.");
    }
  }
}

class RelationPro7 {

  public final static int LEFT = 0;
  public final static int RIGHT = 1;

  private static Map<String, Set<String>> relation;

  public RelationPro7(String user, List<List<String>> friends, List<String> visitors) {
    relation = new HashMap<>();
    findFriend(friends);
    findVisitor(visitors);
  }

  public static Map<String, Set<String>> getRelation() {
    return relation;
  }

  public static void findFriend(List<List<String>> friends) {
    for (List<String> pair : friends) {
      if (!relation.containsKey(pair.get(LEFT))) {
        Set<String> my_friends = new HashSet<>();
        relation.put(pair.get(LEFT), my_friends);
      }
      relation.get(pair.get(LEFT)).add(pair.get(RIGHT));
    }
    for (List<String> pair : friends) {
      if (!relation.containsKey(pair.get(RIGHT))) {
        Set<String> my_friends = new HashSet<>();
        relation.put(pair.get(RIGHT), my_friends);
      }
      relation.get(pair.get(RIGHT)).add(pair.get(LEFT));
    }
  }

  public static void findVisitor(List<String> visitors) {
    for (String name : visitors) {
      if (!relation.containsKey(name)) {
        relation.put(name, null);
      }
    }
  }

  public static void removeMutual(String user, RelationPro7 relation,
      ScorePro7 score) {
    for (String name : RelationPro7.getRelation().get(user)) {
      ScorePro7.getScoreList().remove(name);
    }
  }
}

class ScorePro7 {

  public final static int SCORE_ZERO = 0;
  public final static int SCORE_ONE = 1;
  public final static int SCORE_TEN = 10;

  private static Map<String, Integer> scoreList;

  public static Map<String, Integer> getScoreList() {
    return scoreList;
  }

  public ScorePro7(String user, RelationPro7 relation, List<String> visitors) {
    scoreList = new HashMap<>();
    scoreAddTen(user, relation);
    scoreAddOne(visitors);
  }

  public static void scoreAddTen(String user, RelationPro7 relation) {

    for (String mutual : RelationPro7.getRelation().get(user)) {
      for (String unknown : RelationPro7.getRelation().get(mutual)) {
        if (!unknown.equals(user)) {
          if (!scoreList.containsKey(unknown)) {
            scoreList.put(unknown, SCORE_ZERO);
          }
          int score = scoreList.get(unknown);
          score += SCORE_TEN;
          scoreList.put(unknown, score);
        }
      }
    }
  }

  public static void scoreAddOne(List<String> visitors) {
    for (String visit : visitors) {
      if (!scoreList.containsKey(visit)) {
        scoreList.put(visit, SCORE_ZERO);
      }
      int score = scoreList.get(visit);
      score += SCORE_ONE;
      scoreList.put(visit, score);
    }
  }
}

public class Problem7 {

  public static List<String> solution(String user, List<List<String>> friends,
      List<String> visitors) {

    ValidatorPro7 validator = new ValidatorPro7(user, friends, visitors);
    RelationPro7 relation = new RelationPro7(user, friends, visitors);
    RelationPro7.removeMutual(user, relation, new ScorePro7(user, relation, visitors));
    return new ArrayList<>(ScorePro7.getScoreList().keySet())
        .stream()
        .sorted(((o1, o2) -> ScorePro7.getScoreList().get(o2)
            .compareTo(ScorePro7.getScoreList().get(o1))))
        .limit(5).collect(Collectors.toList());
  }
}
