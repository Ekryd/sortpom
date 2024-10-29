log = new File(basedir, 'build.log')

assert log.exists()
assert log.text.contains('[ERROR] The line separator characters differ from sorted pom')


return true
