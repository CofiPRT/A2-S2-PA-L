JFLAGS =
JC = javac
JVM = java


FILE = Solution
HELPERS = State


ALL_FILES = $(FILE) $(HELPERS)

.SUFFIXES: .java .class

.java.class:
		$(JC) $(JFLAGS) $(addsuffix .java, $(FILE))

default: build

build: $(addsuffix .class, $(FILE))

run: build
	$(JVM) $(FILE)

clean:
	rm -f $(addsuffix .class, $(ALL_FILES))