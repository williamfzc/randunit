# randunit

[![workflow name](https://github.com/williamfzc/randunit/workflows/Android%20CI/badge.svg)](https://github.com/williamfzc/randunit)
[![codecov](https://codecov.io/gh/williamfzc/randunit/branch/main/graph/badge.svg?token=FNCFFQFVP8)](https://codecov.io/gh/williamfzc/randunit)

> Build Android/JVM applications with confidence and less effort.

## what's it?

RandUnit means `Random UnitTest`, which will:

- scan your project (jvm/android) 
- create series of unit cases automatically
- run them with junit 

All you need is a single line of code.

## effect?

As you can see, randunit has been used for testing itself. See the badge above :)

## should use / should not use

In these days, there are still tons of projects developing without any unittest. `Continuous Test` is nearly the most important part of DevOps workflow. 
At the beginning, this repo was designed for providing smoke tests quickly, without any extra efforts.

### should use

Your project is still running naked yet.

### should not use

You want a strict coverage tool.

## license

Licensed under the Apache License, Version 2.0 (the "License"). See [LICENSE](LICENSE).

```text
Copyright 2021 williamfzc

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
