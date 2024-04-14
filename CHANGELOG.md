# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- Added Katz Centrality (`KATZ`) metric for methods.

### Changed

- Increased the language parsing level to 17 to use the full capabilities of javaparser.

## [1.1.1] - 2024-04-05

### Changed

- Moved to Java 17 to support newer dependency versions.
- Moved to javaparser 3.25.10 to fix a `StackOverflowError` triggered by resolving a method statically imported from a parent class.

### Fixed

- Fixed a crash triggered by method calls in local classes.
- Improved correctness when dealing with default interface methods.

## [1.1.0] - 2024-03-30

### Added

- Added the `regexp` CLI option.

## [1.0.0] - 2024-03-24

### Added

- Project renamed to codemetrics4j from [kdunee/jasome](https://github.com/kdunee/jasome) at [9b6e86bcd86b08d7ecfeedd439ac0efa4be54c2d](https://github.com/kdunee/jasome/commit/9b6e86bcd86b08d7ecfeedd439ac0efa4be54c2d).
