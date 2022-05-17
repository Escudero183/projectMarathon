export default [
  {
    path: '/app/proceso/empresa',
    name: 'rt-proceso-empresas',
    component: () => import('@/views/erp/proceso/empresa/EmpresaList.vue'),
    meta: {
      resource: 'rt-proceso-empresas',
      pageTitle: 'Empresa',
      breadcrumb: [
        {
          text: 'Proceso',
        },
        {
          text: 'Empresa',
          active: true,
        },
      ],
    },
  },
]
