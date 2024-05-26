import com.sun.jna.Memory
import org.cameron.cs.{LinuxIOService, LinuxMemoryService}
import zio.*
import zio.test.*
import zio.test.Assertion.*

object LinuxIOServiceSpec extends ZIOSpecDefault {

  def spec = suite("LinuxIOServiceSpec")(
    test("open and close should work correctly") {
      for {
        _      <- ZIO.attempt(new java.io.File("/tmp/testfile").createNewFile())
        fd     <- LinuxIOService.open("/tmp/testfile", 0)
        result <- LinuxIOService.close(fd)
      } yield assert(result)(equalTo(0)) // close should return 0 on success
    },
    test("read and write should work correctly") {
      val value = 12345678L
      for {
        _          <- ZIO.attempt(new java.io.File("/tmp/testfile").createNewFile())
        fd         <- LinuxIOService.open("/tmp/testfile", 2) // O_RDWR flag
        source     <- ZIO.succeed(new Memory(8))
        _          = source.setLong(0, value)
        _          <- LinuxIOService.write(fd, source, 8)
        _          <- LinuxIOService.lseek(fd, 0, 0) // Seek to the start of the file
        readBuffer <- ZIO.succeed(new Memory(8))
        _          <- LinuxIOService.read(fd, readBuffer, 8)
        readValue  = readBuffer.getLong(0)
        _          <- LinuxIOService.close(fd)
      } yield assert(readValue)(equalTo(value)) // read value should match the written value
    }
  ).provideLayer(LinuxIOService.live)
}