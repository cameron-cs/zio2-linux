package org.cameron.cs

import com.sun.jna.{Library, Memory, Native, Pointer}

trait CLib extends Library {

  // Memory operations
  def malloc(size: Int): Memory
  def calloc(num: Long, size: Long): Pointer
  def realloc(ptr: Pointer, newSize: Int): Pointer
  def free(pointer: Pointer): Unit
  def memcpy(dest: Pointer, src: Pointer, n: Long): Pointer
  def memset(dest: Pointer, c: Int, n: Long): Pointer
  def memcmp(s1: Pointer, s2: Pointer, n: Long): Int

  // IO operations
  def close(fd: Int): Int
  def open(pathname: String, flags: Int): Int
  def ioctl(fd: Int, request: Long, arg: Long): Int
  def lseek(fd: Int, offset: Long, whence: Int): Long
  def lseek64(fd: Int, offset: Long, whence: Int): Long
  def open64(pathname: String, flags: Int): Int
  def openat64(dirfd: Int, pathname: String, flags: Int): Int
  def read(fd: Int, buf: Pointer, count: Long): Long
  def write(fd: Int, buf: Pointer, count: Long): Long
}

object CLib {

  val INSTANCE: CLib = Native.load("c", classOf[CLib])
}