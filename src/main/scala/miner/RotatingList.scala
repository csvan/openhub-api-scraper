package miner

/**
 * Created by christopher on 23/11/14.
 */
class RotatingList[T](list: List[T]) {

  private var current = 0

  def next(): T = {

    val result = list(current)

    if (current >= list.length - 1)
      current = 0
    else
      current += 1

    result
  }
}
