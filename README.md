# SatProp
Graphical User Interface to propagate Earth Orbiting Objects based on the java library [orekit](https://www.orekit.org/).

## Features
  - Propagation of Two Line Element (TLE) sets using SGP4 theory.
  - Numerical propagation of orbits with a user selected force model.
  - Available force model:
    - Two body problem with Earth as primary.
    - Non-spherical gravity field of the Earth.
    - Third body perturbations (Sun, Moon, Planets and Pluto).
    - Solar Radiation Pressure.
    - Atmospheric Drag (Harris-Priester Atmospheric Model).
  - Orbit definition based on:
    - Cartesian State vectors (multiple frames of references).
    - Classical orbital elements.
    - TLEs
  - Output in CCSDS standard Orbit Ephemeris Message (OEM).
## Usage
   - Define the initial orbit in the Satellite State tab.
   - Select the perturbations in the Force Model tab.
   - In the Configuration tab, select:
        - Path to the orekit data folder (see [Orekit configuration](https://www.orekit.org/forge/projects/orekit/wiki/Configuration) for more details).
        - Propagation time.
        - Output frame.
        - Output file name
    - Press run! 


## TO DO
  - [ ] Add logo.
  - [ ] Create installation script.
  - [ ] Write documentation.
  - [ ] Create Examples.
  - [ ] Fetch orekit data automatically.

## Contributing

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D


## License

MIT License

Copyright (c) 2018 Oscar Rodriguez Fernandez 

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.