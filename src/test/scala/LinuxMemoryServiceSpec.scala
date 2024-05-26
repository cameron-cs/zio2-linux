import com.sun.jna.{Memory, Pointer}
import org.cameron.cs.LinuxMemoryService
import zio.*
import zio.test.*
import zio.test.Assertion.*

object LinuxMemoryServiceSpec extends ZIOSpecDefault {

  def spec = suite("LinuxMemoryServiceSpec")(
    test("memcpy should copy memory correctly") {
      val value = 12345678L
      for {
        source      <- LinuxMemoryService.malloc(8)
        dest        <- LinuxMemoryService.malloc(8)
        _           = source.setLong(0, value)
        _           <- LinuxMemoryService.memcpy(dest, source, 8)
        copiedValue = dest.getLong(0)
      } yield assert(copiedValue)(equalTo(value)) // copied value should match the source value
    },
    test("calloc should allocate memory") {
      for {
        mem   <- LinuxMemoryService.calloc(1L, 8L)
        _     = mem.setLong(0, 12345678L)
        value = mem.getLong(0)
        _     <- LinuxMemoryService.free(mem)
      } yield assert(value)(equalTo(12345678L))
    },
    test("realloc should reallocate memory") {
      val value = 12345678L
      for {
        mem          <- LinuxMemoryService.calloc(1L, 8L)
        _            = mem.setLong(0, value)
        reallocMem   <- LinuxMemoryService.realloc(mem, 16)
        reallocValue = reallocMem.getLong(0)
        _            <- LinuxMemoryService.free(reallocMem)
      } yield assert(reallocValue)(equalTo(value))
    },
    test("memset should set memory correctly") {
      for {
        ptr   <- LinuxMemoryService.calloc(1L, 8L)
        _     <- LinuxMemoryService.memset(ptr, 0xFF, 8)
        value = (0 until 8).map(ptr.getByte(_).toInt & 0xFF).toArray
        _     <- LinuxMemoryService.free(ptr)
      } yield assert(value)(equalTo(Array.fill(8)(0xFF)))
    }
  ).provideLayer(LinuxMemoryService.live)
}