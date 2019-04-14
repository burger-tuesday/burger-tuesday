import * as React from 'react';
import {ErrorInfo} from 'react';

interface IErrorBoundaryProps {
  readonly children: JSX.Element | JSX.Element[];
}

interface IErrorBoundaryState {
  readonly error: Error | undefined;
  readonly errorInfo: ErrorInfo | undefined;
}

class ErrorBoundary extends React.Component<IErrorBoundaryProps, IErrorBoundaryState> {
  public readonly state: IErrorBoundaryState = { error: undefined, errorInfo: undefined };

  public componentDidCatch(error: Error, errorInfo: ErrorInfo) {
    this.setState({
      error,
      errorInfo
    });
  }

  public render() {
    const { error, errorInfo } = this.state;
    if (errorInfo) {
      const errorDetails =
        process.env.NODE_ENV === 'development' ? (
          <details className="preserve-space">
            {error && error.toString()}
            <br />
            {errorInfo.componentStack}
          </details>
        ) : (
          undefined
        );
      return (
        <div>
          <h2 className="error">An unexpected error has occurred.</h2>
          {errorDetails}
        </div>
      );
    }
    return this.props.children;
  }
}

export default ErrorBoundary;
