import { Moment } from 'moment';

export const enum ProductTypeEnum {
    PRODUTO = 'PRODUTO',
    SERVICO = 'SERVICO'
}

export interface IGeneralCatalog {
    id?: number;
    productName?: string;
    productDescription?: string;
    productPrice?: number;
    productType?: ProductTypeEnum;
    productImgContentType?: string;
    productImg?: any;
}

export class GeneralCatalog implements IGeneralCatalog {
    constructor(
        public id?: number,
        public productName?: string,
        public productDescription?: string,
        public productPrice?: number,
        public productType?: ProductTypeEnum,
        public productImgContentType?: string,
        public productImg?: any
    ) {}
}
