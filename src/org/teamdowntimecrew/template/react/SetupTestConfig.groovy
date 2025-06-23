package org.teamdowntimecrew.template.react

class SetupTestConfig implements Serializable {
    def steps

    SetupTestConfig(steps) {
        this.steps = steps
    }

    def run() {
        steps.stage('Setup Test Config') {
            steps.sh '''
                npm pkg set scripts.test="jest --json --outputFile=test-reports/test-results.json"
                npm pkg set scripts.test:watch="jest --watch"

                cat > babel.config.js <<'EOL'
module.exports = {
  presets: [
    '@babel/preset-env',
    '@babel/preset-react'
  ]
};
EOL

                cat > jest.config.js <<'EOL'
module.exports = {
  testEnvironment: 'jsdom',
  setupFilesAfterEnv: ['./jest.setup.js'],
  moduleNameMapper: {
    "\\\\.(css|less|scss)\$": "identity-obj-proxy"
  },
  testPathIgnorePatterns: [
    '/node_modules/',
    '/public/'
  ],
  transform: {
    '^.+\\\\.(js|jsx)$': 'babel-jest'
  }
};
EOL

                echo "require('@testing-library/jest-dom/extend-expect');" > jest.setup.js

                mkdir -p src/components
                if [ ! -f src/components/ExampleComponent.js ]; then
                  cat > src/components/ExampleComponent.js <<'EOL'
import React from 'react';

const ExampleComponent = ({ text }) => {
  return <div>{text}</div>;
};

export default ExampleComponent;
EOL
                fi

                mkdir -p src/components/tests
                if [ ! -f src/components/tests/ExampleComponent.test.js ]; then
                  cat > src/components/tests/ExampleComponent.test.js <<'EOL'
import React from 'react';
import { render, screen } from '@testing-library/react';
import ExampleComponent from '../ExampleComponent';

test('renders the passed text', () => {
  render(<ExampleComponent text="Test Content" />);
  expect(screen.getByText('Test Content')).toBeInTheDocument();
});
EOL
                fi
            '''
        }
    }
}
