JFLAGS =
JC = javac
JVM = java


FILE = Main
HELPERS = 'Main$$Task' 'Main$$Edge'


IN_FILE = in
OUT_FILE = out


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