JFLAGS =
JC = javac
JVM = java


FILE = Main
HELPERS = 'Main$$Task' 'Main$$Task$$Edge'


IN_FILE = in
OUT_FILE = out


ALL_FILES = $(FILE) $(HELPERS)

.PHONY: default build run clean

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $(addsuffix .java,$(FILE))

default: build

build: $(addsuffix .class,$(FILE))

run:
	$(JVM) $(FILE)

clean:
	rm -f $(addsuffix .class,$(ALL_FILES))