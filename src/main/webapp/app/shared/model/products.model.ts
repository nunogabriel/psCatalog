import { Moment } from 'moment';
import { IOrderDet } from 'app/shared/model//order-det.model';
import { ICustomers } from 'app/shared/model//customers.model';

export const enum ProductTypeEnum {
    PRODUTO = 'PRODUTO',
    SERVICO = 'SERVICO'
}

export interface IProducts {
    id?: number;
    productName?: string;
    productDescription?: string;
    productPrice?: number;
    productStartDate?: Moment;
    productEndDate?: Moment;
    productType?: ProductTypeEnum;
    productImgContentType?: string;
    productImg?: any;
    createdBy?: string;
    createdDate?: Moment;
    lastModifiedBy?: string;
    lastModifiedDate?: Moment;
    productQuantity?: number;
    orderDets?: IOrderDet[];
    supplierSupplierName?: string;
    supplierId?: number;
    customers?: ICustomers[];
}

export class Products implements IProducts {
    constructor(
        public id?: number,
        public productName?: string,
        public productDescription?: string,
        public productPrice?: number,
        public productStartDate?: Moment,
        public productEndDate?: Moment,
        public productType?: ProductTypeEnum,
        public productImgContentType?: string,
        public productImg?: any,
        public createdBy?: string,
        public createdDate?: Moment,
        public lastModifiedBy?: string,
        public lastModifiedDate?: Moment,
        public productQuantity?: number,
        public orderDets?: IOrderDet[],
        public supplierSupplierName?: string,
        public supplierId?: number,
        public customers?: ICustomers[]
    ) {}
}
