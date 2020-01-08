log = new File(basedir, 'build.log')
expected_log = new File(basedir, 'expected.log')

assert log.exists()
assert log.text.replaceAll('\r','').contains(expected_log.text.replaceAll('@pom.version@', projectversion).replaceAll('\r',''))

return true