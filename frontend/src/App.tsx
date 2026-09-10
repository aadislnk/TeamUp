import { Routes, Route, Navigate } from 'react-router-dom';
import { RegisterPage, OtpPage, LoginPage } from './pages';
import { PublicOnlyRoute, ProtectedRoute } from './routes';
import { useAuth } from './hooks';
import { Card, CardHeader, CardTitle, CardDescription, CardContent, Button } from './components/ui';

/**
 * Minimal authenticated landing page placeholder.
 * Serves as the initial protected route destination until feature modules are implemented.
 */
function AuthenticatedHome() {
  const { user, logout } = useAuth();

  return (
    <main className="min-h-screen flex flex-col justify-center items-center px-4 py-8 bg-background">
      <div className="w-full max-w-md">
        <Card className="w-full">
          <CardHeader className="pb-4">
            <CardTitle className="text-xl">Welcome to TeamUp</CardTitle>
            <CardDescription>
              Authenticated session active for{' '}
              <span className="font-medium text-foreground">{user?.fullName || user?.email}</span>
            </CardDescription>
          </CardHeader>
          <CardContent className="flex flex-col gap-4 text-sm text-muted">
            <div className="p-3.5 bg-slate-50 border border-slate-200 rounded-default text-foreground font-medium">
              Email: {user?.email}
            </div>
            <p>
              Your account is authenticated and verified. Application feature modules will be connected in subsequent implementation phases.
            </p>
            <Button
              type="button"
              variant="outline"
              size="md"
              onClick={logout}
              className="w-full mt-1"
            >
              Log out
            </Button>
          </CardContent>
        </Card>
      </div>
    </main>
  );
}


function App() {
  return (
    <Routes>
      {/* Public / Auth routes (only accessible when unauthenticated) */}
      <Route element={<PublicOnlyRoute />}>
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/verify-otp" element={<OtpPage />} />
        <Route path="/login" element={<LoginPage />} />
      </Route>

      {/* Protected routes (only accessible when authenticated) */}
      <Route element={<ProtectedRoute />}>
        <Route path="/" element={<AuthenticatedHome />} />
      </Route>

      {/* Fallback route */}
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}

export default App;