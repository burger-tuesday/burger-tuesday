import * as Sentry from '@sentry/browser';
import * as React from 'react';
import {ErrorInfo} from 'react';
import authStore from '../../stores/authStore';


interface IErrorBoundaryProps {
  readonly children: JSX.Element | JSX.Element[];
}

interface IErrorBoundaryState {
  readonly error: Error | undefined;
  readonly errorInfo: ErrorInfo | undefined;
  readonly eventId: string | undefined;
}

class ErrorBoundary extends React.Component<IErrorBoundaryProps, IErrorBoundaryState> {
  public readonly state: IErrorBoundaryState = {
    error: undefined,
    errorInfo: undefined,
    eventId: undefined
  };

  public componentDidCatch(error: Error, errorInfo: ErrorInfo) {
    this.setState({
      error,
      errorInfo
    });
    Sentry.withScope(scope => {
      if (authStore.userProfile) {
        scope.setUser({
          username: authStore.userProfile.username,
          email: authStore.userProfile.email,
          id: authStore.userProfile.user_id
        })
      }
      scope.setExtras(errorInfo);
      const eventId = Sentry.captureException(error);
      this.setState({eventId})
    });
  }

  public render() {
    const {error, errorInfo} = this.state;
    if (errorInfo) {
      const errorDetails =
          process.env.NODE_ENV === 'development' ? (
              <details className="preserve-space">
                {error && error.toString()}
                <br/>
                {errorInfo.componentStack}
              </details>
          ) : (
              undefined
          );
      return (
          <div>
            <h2 className="error">An unexpected error has occurred.</h2>
            <a onClick={() => Sentry.showReportDialog({eventId: this.state.eventId})}>Report
              feedback</a>
            {errorDetails}
          </div>
      );
    }
    return this.props.children;
  }
}

export default ErrorBoundary;
