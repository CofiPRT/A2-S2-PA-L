JFLAGS =
JC = javac
JVM = java


FILE = Solution
HELPERS = Coord


IN_FILE = in
OUT_FILE = out


ALL_FILES = $(FILE) $(HELPERS)

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $(addsuffix .java,$(FILE))

default: build

build: $(addsuffix .class,$(FILE))

run:
	$(JVM) $(FILE)

run_file:
	$(JVM) $(FILE) < $(IN_FILE) > $(OUT_FILE)

clean:
	rm -f $(addsuffix .class,$(ALL_FILES))