package mn.sict;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomSorter implements CardOrganizer{
  @Override
  public List<Card> organize(List<Card > cards){
    List<Card> shuffled = new ArrayList<>(cards);
    Collections.shuffle(shuffled);
    return shuffled;
  }
}