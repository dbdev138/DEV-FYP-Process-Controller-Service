//case class Stats(runtime_pc: Int,runtime_bbds: Int, runtime_gts: Int)
package com.models

import scala.collection.mutable.ListBuffer

trait RuntimeStats

object RuntimeStats{
    
    case class RuntimeStats(num_records_returned: Int, runtime_pc: Int, runtime_bbds: Int, runtime_gts: Int, runtime_ss: Int)
    
    val stats = ListBuffer[RuntimeStats](
        RuntimeStats(9, 6970, 69, 6853, 666),
        RuntimeStats(9, 6970, 69, 6853, 666),
        RuntimeStats(9, 6970, 69, 6853, 666),
        RuntimeStats(9, 6970, 69, 6853, 666),
        RuntimeStats(9, 6970, 69, 6853, 666)
      )

}