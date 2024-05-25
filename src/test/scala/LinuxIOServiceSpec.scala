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
        fd     <- LinuxIOService.open("/tmp/testfile", 0).provideLayer(LinuxIOService.live)
        result <- LinuxIOService.close(fd).provideLayer(LinuxIOService.live)
      } yield assert(result)(equalTo(0)) // close should return 0 on success
    },
    test("read and write should work correctly") {
      val value = 12345678L
      for {
        _          <- ZIO.attempt(new java.io.File("/tmp/testfile").createNewFile())
        fd         <- LinuxIOService.open("/tmp/testfile", 2).provideLayer(LinuxIOService.live) // O_RDWR flag
        source     <- ZIO.succeed(new Memory(8))
        _          = source.setLong(0, value)
        _          <- LinuxIOService.write(fd, source, 8).provideLayer(LinuxIOService.live)
        _          <- LinuxIOService.lseek(fd, 0, 0).provideLayer(LinuxIOService.live) // Seek to the start of the file
        readBuffer <- ZIO.succeed(new Memory(8))
        _          <- LinuxIOService.read(fd, readBuffer, 8).provideLayer(LinuxIOService.live)
        readValue  = readBuffer.getLong(0)
        _          <- LinuxIOService.close(fd).provideLayer(LinuxIOService.live)
      } yield assert(readValue)(equalTo(value)) // read value should match the written value
    }
  )
}