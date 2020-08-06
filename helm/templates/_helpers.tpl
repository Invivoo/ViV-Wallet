{{/*
Common labels
*/}}
{{- define "labels" -}}
{{- include "labels.noversion" . }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end -}}

{{/*
Common labels without version (useful for services)
*/}}
{{- define "labels.noversion" -}}
app.kubernetes.io/name: {{ .Chart.Name }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/component: {{ .Name | quote }}
{{- end -}}

{{/*
Default liveness probe strategy
*/}}
{{- define "livenessProbe" -}}
livenessProbe:
  httpGet:
    path: /api/ServiceStatus
    port: 8080
  failureThreshold: 3
  periodSeconds: 10
  initialDelaySeconds: 10
  timeoutSeconds: 5
{{- end -}}

{{/*
Rolling strategy
*/}}
{{- define "strategy.rolling" -}}
strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: {{ .Values.strategy.rollingUpdate.maxSurge }} 
      maxUnavailable: {{ .Values.strategy.rollingUpdate.maxUnavailable }} 
{{- end -}}

{{/*
Resources limits
*/}}
{{- define "resources.large" -}}
resources:
    requests:
        cpu: {{ .Values.resources.large.cpu.request | quote }} 
        memory: {{ .Values.resources.large.memory.request | quote }} 
    limits:
        cpu: {{ .Values.resources.large.cpu.limit | quote }} 
        memory: {{ .Values.resources.large.memory.limit | quote }} 
{{- end -}}

{{- define "resources.medium" -}}
resources:
    requests:
        cpu: {{ .Values.resources.medium.cpu.request | quote }} 
        memory: {{ .Values.resources.medium.memory.request | quote }} 
    limits:
        cpu: {{ .Values.resources.medium.cpu.limit | quote }} 
        memory: {{ .Values.resources.medium.memory.limit | quote }} 
{{- end -}}

{{- define "resources.small" -}}
resources:
    requests:
        cpu: {{ .Values.resources.small.cpu.request | quote }} 
        memory: {{ .Values.resources.small.memory.request | quote }} 
    limits:
        cpu: {{ .Values.resources.small.cpu.limit | quote }} 
        memory: {{ .Values.resources.small.memory.limit | quote }} 
{{- end -}}

{{/*
Recreate strategy
*/}}
{{- define "strategy.recreate" -}}
strategy:
    type: Recreate
{{- end -}}

{{/*
Endpoint env variables
*/}}

{{- define "protocol" -}}
{{- if .Values.ingress.tls }}{{ print "https" }}{{- else }}{{ print "http" }}{{- end -}}
{{- end -}}

{{- define "login.url" -}}
{{- if eq .Values.ingress.class "addon-http-application-routing" }}{{ print "login." (default .Release.Name .Values.global.devSubDomain) "." .Values.global.clusterSpecificDnszone }}{{- else }}{{ print .Values.global.loginSpecificDnszone }}{{- end -}}
{{- end -}}

{{- define "apps.url" -}}
{{- if eq .Values.ingress.class "addon-http-application-routing" }}{{ print "apps." (default .Release.Name .Values.global.devSubDomain) "." .Values.global.clusterSpecificDnszone }}{{- else }}{{ print .Values.global.appsSpecificDnszone }}{{- end -}}
{{- end -}}

{{- define "idp.url" -}}
{{- if eq .Values.ingress.class "addon-http-application-routing" }}{{ print "idp." (default .Release.Name .Values.global.devSubDomain) "." .Values.global.clusterSpecificDnszone }}{{- else }}{{ print .Values.global.idpSpecificDnszone }}{{- end -}}
{{- end -}}

{{- define "ingress.tls" -}}
{{- if .Values.ingress.tls -}}
tls:
- hosts:
  - {{ include "login.url" .}}
  secretName: login-tls-secret
- hosts:
  - {{ include "apps.url" .}}
  secretName: apps-tls-secret
{{- print "\n" -}}
{{- end -}}
{{- end -}}

{{- define "ingress.issuer" -}}
{{- if .Values.ingress.tls -}}
cert-manager.io/cluster-issuer: letsencrypt-issuer
{{- print "\n" -}}
{{- end -}}
{{- end -}}
