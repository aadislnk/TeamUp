import { Routes, Route, Navigate } from 'react-router-dom';
import { RegisterPage, OtpPage, LoginPage } from './pages';

function App() {
  return (
    <Routes>
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/verify-otp" element={<OtpPage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/" element={<Navigate to="/register" replace />} />
      <Route path="*" element={<Navigate to="/register" replace />} />
    </Routes>
  );
}

export default App;