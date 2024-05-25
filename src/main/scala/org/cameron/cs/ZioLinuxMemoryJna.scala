package org.cameron.cs

import com.sun.jna.{Memory, Pointer}
import zio.{Task, *}

object ZioLinuxMemoryJna {

  def malloc(size: Long): Task[Memory] =
    ZIO.attempt(new Memory(size))

  def calloc(num: Long, size: Long): Task[Pointer] =
    ZIO.attempt {
      val ptr = CLib.INSTANCE.calloc(num, size)

      if (ptr == null)
        throw new OutOfMemoryError(s"Cannot allocate ${num * size} bytes")

      ptr
    }

  def realloc(ptr: Pointer, newSize: Int): Task[Pointer] =
    ZIO.attempt {
      val newPtr = CLib.INSTANCE.realloc(ptr, newSize)

      if (newPtr == null)
        throw new OutOfMemoryError(s"Cannot reallocate to $newSize bytes")
      newPtr
    }

  def free(ptr: Pointer): Task[Unit] =
    ZIO.attempt(CLib.INSTANCE.free(ptr))

  def memcpy(dest: Pointer, src: Pointer, n: Long): Task[Pointer] =
    ZIO.attempt(CLib.INSTANCE.memcpy(dest, src, n))

  def memset(dest: Pointer, c: Int, n: Long): Task[Pointer] =
    ZIO.attempt(CLib.INSTANCE.memset(dest, c, n))

  def memcmp(s1: Pointer, s2: Pointer, n: Long): Task[Int] =
    ZIO.attempt(CLib.INSTANCE.memcmp(s1, s2, n))
}
