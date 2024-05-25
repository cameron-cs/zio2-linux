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
        source      <- LinuxMemoryService.malloc(8).provideLayer(LinuxMemoryService.live)
        dest        <- LinuxMemoryService.malloc(8).provideLayer(LinuxMemoryService.live)
        _           = source.setLong(0, value)
        _           <- LinuxMemoryService.memcpy(dest, source, 8).provideLayer(LinuxMemoryService.live)
        copiedValue = dest.getLong(0)
      } yield assert(copiedValue)(equalTo(value)) // copied value should match the source value
    },

    test("calloc should allocate memory") {
      for {
        mem   <- LinuxMemoryService.calloc(1L, 8L).provideLayer(LinuxMemoryService.live)
        _     = mem.setLong(0, 12345678L)
        value = mem.getLong(0)
        _     <- LinuxMemoryService.free(mem).provideLayer(LinuxMemoryService.live)
      } yield assert(value)(equalTo(12345678L))
    },

    test("realloc should reallocate memory") {
      val value = 12345678L
      for {
        mem          <- LinuxMemoryService.calloc(1L, 8L).provideLayer(LinuxMemoryService.live)
        _            = mem.setLong(0, value)
        reallocMem   <- LinuxMemoryService.realloc(mem, 16).provideLayer(LinuxMemoryService.live)
        reallocValue = reallocMem.getLong(0)
        _            <- LinuxMemoryService.free(reallocMem).provideLayer(LinuxMemoryService.live)
      } yield assert(reallocValue)(equalTo(value))
    },

    test("memset should set memory correctly") {
      for {
        ptr   <- LinuxMemoryService.calloc(1L, 8L).provideLayer(LinuxMemoryService.live)
        _     <- LinuxMemoryService.memset(ptr, 0xFF, 8).provideLayer(LinuxMemoryService.live)
        value = (0 until 8).map(ptr.getByte(_).toInt & 0xFF).toArray
        _     <- LinuxMemoryService.free(ptr).provideLayer(LinuxMemoryService.live)
      } yield assert(value)(equalTo(Array.fill(8)(0xFF)))
    }
  )
}